package server.yogoyogu.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.yogoyogu.dto.board.BoardCreateRequestDto;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.dto.board.BoardFindAllResponseDto;
import server.yogoyogu.dto.board.BoardResponseDto;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.likes.Likes;
import server.yogoyogu.entity.member.Authority;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.exception.BoardNotFoundException;
import server.yogoyogu.exception.BoardOnlyWriteStudentException;
import server.yogoyogu.exception.MemberNotEqualsException;
import server.yogoyogu.repository.board.BoardRepository;
import server.yogoyogu.repository.likes.LikesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public void create(BoardCreateRequestDto req, Member member) {
        validateAuthority(member);
        Board board = new Board(member, req.getTitle(), req.getContent());
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<BoardFindAllResponseDto> findAll() {
        List<Board> boards = boardRepository.findAll();
        List<BoardFindAllResponseDto> result = new ArrayList<>();
        boards.stream().map(i -> result.add(new BoardFindAllResponseDto().toDto(i))).collect(Collectors.toList());
        return result;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto find(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        return new BoardResponseDto().toDto(board);
    }

    @Transactional
    public boolean likeOrUnlike(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        boolean isLiked = checkIsLiked(board, member);

        if(isLiked) {
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
        System.out.println(board.getTitle());
    }

    @Transactional
    public void delete(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateOwnBoard(board, member);
        boardRepository.delete(board);
    }

    public void validateAuthority(Member member) {
        if (member.getAuthority().equals(Authority.ROLE_MANAGER)) {
            throw new BoardOnlyWriteStudentException();
        }
    }

    public void validateOwnBoard(Board board, Member member) {
        if (!board.getMember().equals(member)) {
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
