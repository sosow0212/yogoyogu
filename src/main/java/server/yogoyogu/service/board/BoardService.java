package server.yogoyogu.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.yogoyogu.dto.board.*;
import server.yogoyogu.dto.reply.ReplyCreateRequestDto;
import server.yogoyogu.dto.reply.ReplyEditRequestDto;
import server.yogoyogu.dto.reply.ReplyResponseDto;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.likes.Likes;
import server.yogoyogu.entity.member.Authority;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.reply.Reply;
import server.yogoyogu.exception.*;
import server.yogoyogu.repository.board.BoardRepository;
import server.yogoyogu.repository.likes.LikesRepository;
import server.yogoyogu.repository.reply.ReplyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void create(BoardCreateRequestDto req, Member member) {
        validateAuthorityIsUser(member);
        Authority tag = selectTag(req.getTag());
        Board board = new Board(member, req.getTitle(), req.getContent(), tag);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public BoardFindAllResponseDto findAll(String sort, Integer page, Member member) {
        PageRequest pageRequest;
        if (sort.equals("likesCount")) {
            pageRequest = PageRequest.of(page, 10, Sort.by(sort).descending().and(Sort.by("id")));
        } else {
            pageRequest = PageRequest.of(page, 10, Sort.by(sort).descending());
        }


        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardSimpleDto> boardSimpleDtos = new ArrayList<>();

        boards.stream().map(i -> boardSimpleDtos.add(BoardSimpleDto.toDto(i, likesRepository.existsByMemberAndBoard(member, i)))).collect(Collectors.toList());

        PageInfoDto pageInfoDto = new PageInfoDto(boards);
        BoardFindAllResponseDto result = new BoardFindAllResponseDto(boardSimpleDtos, pageInfoDto);
        return result;
    }

    @Transactional(readOnly = true)
    public BoardAndReplyResponseDto find(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Reply reply = replyRepository.existsByBoard(board) ? replyRepository.findByBoard(board).get() : null;

        boolean isAlreadyPushedLikeByUser = likesRepository.existsByMemberAndBoard(member, board);
        return new BoardAndReplyResponseDto().toDto(board, reply, member, isAlreadyPushedLikeByUser);
    }

    @Transactional
    public boolean likeOrUnlike(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        boolean isLiked = checkIsLiked(board, member);

        if (isLiked) {
            // 좋아요 처리된 경우 => 좋아요 취소
            Likes like = likesRepository.findByMemberAndBoard(member, board).get();
            likesRepository.delete(like);
            board.unliked();
            return false;
        } else {
            // 좋아요 없는 경우 => 좋아요 처리
            likesRepository.save(new Likes(member, board));
            board.liked();
            return true;
        }
    }

    @Transactional
    public void edit(Long id, BoardEditRequestDto req, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateOwnBoard(board, member);
        board.editBoard(req);
    }

    @Transactional
    public void delete(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateOwnBoard(board, member);
        boardRepository.delete(board);
    }

    // 학생회

    @Transactional
    public void createReply(ReplyCreateRequestDto req, Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        validateBoardTagEqualsMemberAuth(member, board);
        validateAlreadyWroteReply(board);

        board.repliedSuccess();
        Reply reply = new Reply(member, req.getContent(), board);

        replyRepository.save(reply);
    }

    @Transactional(readOnly = true)
    public ReplyResponseDto findReply(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Reply reply = replyRepository.findByBoard(board).orElseThrow(ReplyNotFoundException::new);
        ReplyResponseDto result = ReplyResponseDto.toDto(reply);
        return result;
    }

    @Transactional
    public void editReply(Long boardId, ReplyEditRequestDto req, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Reply reply = replyRepository.findByBoard(board).orElseThrow(BoardNotFoundException::new);
        validateOwnReply(reply, member);
        reply.setContent(req.getContent());
    }

    public Authority selectTag(String tag) {
        if (tag.equals("none")) {
            return Authority.ROLE_ANY;
        } else if (tag.equals("인문캠")) {
            return Authority.ROLE_SEOUL_MANAGER;
        } else if (tag.equals("자연캠")) {
            return Authority.ROLE_YONGIN_MANAGER;
        } else if (tag.equals("총학생회")) {
            return Authority.ROLE_MANAGER;
        } else {
            return Authority.ROLE_ANY;
        }
    }

    public void validateBoardTagEqualsMemberAuth(Member member, Board board) {
        Authority boardAuth = board.getTag();
        Authority memberAuth = member.getAuthority();
        if(boardAuth == Authority.ROLE_ANY) {
            return;
        }
        if (boardAuth != memberAuth) {
            throw new ReplyOnlyWriteStudentManagerException();
        }
    }

    public void validateAuthorityIsUser(Member member) {
        if (!member.getAuthority().equals(Authority.ROLE_USER)) {
            throw new BoardOnlyWriteStudentException();
        }
    }

    public void validateAlreadyWroteReply(Board board) {
        if (replyRepository.existsByBoard(board)) {
            throw new ReplyAlreadyWroteException();
        }
    }

    public void validateOwnBoard(Board board, Member member) {
        if (!board.getMember().equals(member)) {
            throw new MemberNotEqualsException();
        }
    }

    public void validateOwnReply(Reply reply, Member member) {
        if (!reply.getMember().equals(member)) {
            throw new MemberNotEqualsException();
        }
    }

    public boolean checkIsLiked(Board board, Member member) {
        if (likesRepository.findByMemberAndBoard(member, board).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


}
