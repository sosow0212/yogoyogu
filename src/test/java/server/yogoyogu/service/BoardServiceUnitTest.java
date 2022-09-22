package server.yogoyogu.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.yogoyogu.dto.board.BoardCreateRequestDto;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.dto.board.BoardFindAllResponseDto;
import server.yogoyogu.dto.board.BoardResponseDto;
import server.yogoyogu.dto.reply.ReplyCreateRequestDto;
import server.yogoyogu.dto.reply.ReplyEditRequestDto;
import server.yogoyogu.dto.reply.ReplyResponseDto;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.likes.Likes;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.reply.Reply;
import server.yogoyogu.repository.board.BoardRepository;
import server.yogoyogu.repository.likes.LikesRepository;
import server.yogoyogu.repository.reply.ReplyRepository;
import server.yogoyogu.service.board.BoardService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static server.yogoyogu.factory.BoardFactory.createBoard;
import static server.yogoyogu.factory.MemberFactory.createManager;
import static server.yogoyogu.factory.MemberFactory.createUser;
import static server.yogoyogu.factory.ReplyFactory.createReply;

@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    LikesRepository likesRepository;

    @Mock
    ReplyRepository replyRepository;

    @Test
    @DisplayName("게시글 생성")
    public void createTest() {
        // given
        Member member = createUser();
        Board board = createBoard(member);
        BoardCreateRequestDto req = new BoardCreateRequestDto("title", "content");

        // when
        boardService.create(req, member);

        // then
        verify(boardRepository).save(any());
    }

    @Test
    @DisplayName("게시글 전체 조회")
    public void findAllTest() {
        // given
        List<Board> boards = List.of(createBoard(createUser()));
        given(boardRepository.findAll()).willReturn(boards);

        // when
        List<BoardFindAllResponseDto> result = boardService.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    public void findTest() {
        // given
        Long id = 1L;
        Board board = createBoard(createUser());
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        // when
        BoardResponseDto result = boardService.find(id);

        // then
        assertThat(result.getContent()).isEqualTo(board.getContent());
    }

    @Test
    @DisplayName("좋아요 및 좋아요 취소 (좋아요 처리)")
    public void likeTest() {
        // given
        boolean ans = true;
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        given(likesRepository.findByMemberAndBoard(member, board)).willReturn(Optional.empty());

        // when
        boolean result = boardService.likeOrUnlike(id, member);

        // then
        assertThat(result).isEqualTo(ans);
    }

    @Test
    @DisplayName("좋아요 및 좋아요 취소 (좋아요 취소 처리)")
    public void unlikeTest() {
        // given
        boolean ans = false;
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        Likes likes = new Likes(member, board);
        given(likesRepository.findByMemberAndBoard(member, board)).willReturn(Optional.of(likes));

        given(likesRepository.findByMemberAndBoard(member, board)).willReturn(Optional.of(likes));

        // when
        boolean result = boardService.likeOrUnlike(id, member);

        // then
        assertThat(result).isEqualTo(ans);
    }


    @Test
    @DisplayName("게시글 수정")
    public void editTest() {
        // given
        Long id = 1L;
        BoardEditRequestDto req = new BoardEditRequestDto("title2", "content2");
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(id)).willReturn(Optional.of(board));


        // when
        boardService.edit(id, req, member);

        // then
        assertThat(board.getTitle()).isEqualTo(req.getTitle());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deleteTest() {
        // given
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        // when
        boardService.delete(id, member);

        // then
        verify(boardRepository).delete(board);
    }

    @Test
    @DisplayName("학생회 답변 등록")
    public void createReplyTest() {
        // given
        Long id = 1L;
        ReplyCreateRequestDto req = new ReplyCreateRequestDto("답장");
        Member member = createManager();
        Board board = createBoard(createUser());

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(replyRepository.existsByBoardAndMember(board, member)).willReturn(false);

        Reply reply = new Reply(member, req.getContent(), board);

        // when
        boardService.createReply(req, id, member);

        // then
        verify(replyRepository).save(reply);
    }

    @Test
    @DisplayName("학생회 답변 조회")
    public void findReplyTest() {
        // given
        Long id = 1L;
        Member member = createManager();
        Board board = createBoard(createUser());
        Reply reply = createReply(board, member);

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(replyRepository.findByBoard(board)).willReturn(Optional.of(reply));

        // when
        ReplyResponseDto result = boardService.findReply(id);

        // then
        assertThat(result.getContent()).isEqualTo(reply.getContent());
    }

    @Test
    @DisplayName("학생회 답변 수정")
    public void editReplyTest() {
        // given
        Long id = 1L;
        Member member = createManager();
        Board board = createBoard(createUser());
        Reply reply = createReply(board, member);
        ReplyEditRequestDto req = new ReplyEditRequestDto("수정");

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(replyRepository.findByBoard(board)).willReturn(Optional.of(reply));


        // when
        boardService.editReply(id, req, member);

        // then
        assertThat(reply.getContent()).isEqualTo(req.getContent());
    }
}
