package server.yogoyogu.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.yogoyogu.controller.board.BoardController;
import server.yogoyogu.dto.board.BoardCreateRequestDto;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.repository.Member.MemberRepository;
import server.yogoyogu.service.board.BoardService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.yogoyogu.factory.MemberFactory.createUser;

@ExtendWith(MockitoExtension.class)
public class BoardControllerUnitTest {

    @InjectMocks
    BoardController boardController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BoardService boardService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    @DisplayName("게시글 생성")
    public void createTest() throws Exception {
        // given
        Long id = 1L;
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        BoardCreateRequestDto req = new BoardCreateRequestDto("title", "content");

        // when
        mockMvc.perform(
                post("/api/boards")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        // then
        verify(boardService).create(req, member);
    }

    @Test
    @DisplayName("게시글 전체 조회")
    public void findAllTest() throws Exception {
        // given

        // when
        mockMvc.perform(
                get("/api/boards")
        ).andExpect(status().isOk());

        // then
        verify(boardService).findAll();
    }

    @Test
    @DisplayName("게시글 단건 조회")
    public void findTest() throws Exception {
        // given
        Long id = 1L;

        // when
        mockMvc.perform(
                get("/api/boards/{id}",id)
        ).andExpect(status().isOk());

        // then
        verify(boardService).find(id);
    }

    @Test
    @DisplayName("게시글 좋아요 및 좋아요 취소")
    public void likeOrUnlikeTest() throws Exception {
        // given
        Long id = 1L;
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                post("/api/boards/{boardId}", id)
        ).andExpect(status().isOk());

        // then
        verify(boardService).likeOrUnlike(id, member);
    }

    @Test
    @DisplayName("게시글 수정")
    public void edit() throws Exception {
        // given
        Long id = 1L;
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));
        BoardEditRequestDto req = new BoardEditRequestDto("수정", "내용 수정");

        // when
        mockMvc.perform(
                put("/api/boards/{boardId}", id)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // then
        verify(boardService).edit(id, req, member);
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deleteTest() throws Exception {
        // given
        Long id = 1L;
        Member member = createUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when
        mockMvc.perform(
                delete("/api/boards/{boardId}", id)
        ).andExpect(status().isOk());

        // then
        verify(boardService).delete(id, member);
    }
}
