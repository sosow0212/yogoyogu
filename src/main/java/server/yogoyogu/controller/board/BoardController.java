package server.yogoyogu.controller.board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import server.yogoyogu.dto.board.BoardCreateRequestDto;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.dto.reply.ReplyCreateRequestDto;
import server.yogoyogu.dto.reply.ReplyEditRequestDto;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.exception.MemberNotFoundException;
import server.yogoyogu.repository.Member.MemberRepository;
import server.yogoyogu.response.Response;
import server.yogoyogu.service.board.BoardService;

import javax.validation.Valid;

@Api(value = "Board Controller", tags = "Board")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시글 생성", notes = "게시글 생성")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards")
    public Response create(@Valid @RequestBody BoardCreateRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        boardService.create(req, member);
        return Response.success();
    }

    @ApiOperation(value = "게시글 전체 목록 조회", notes = "게시글을 전체 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public Response findAll(@RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "0") Integer page) {
        // http://localhost:8080/api/boards?sort=likesCount&page=0
        return Response.success(boardService.findAll(sort, page));
    }

    @ApiOperation(value = "게시글 상세 조회", notes = "게시글을 상세 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/{boardId}")
    public Response find(@PathVariable("boardId") Long boardId) {
        return Response.success(boardService.find(boardId));
    }


    @ApiOperation(value = "게시글 좋아요 및 좋아요 취소 처리", notes = "게시글을 좋아요 및 좋아요 취소 처리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/{boardId}")
    public Response likeOrUnlike(@PathVariable("boardId") Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return Response.success(boardService.likeOrUnlike(boardId, member));
    }


    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/{boardId}")
    public Response edit(@PathVariable("boardId") Long boardId, @Valid @RequestBody BoardEditRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        boardService.edit(boardId, req, member);
        return Response.success();
    }


    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다..")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/{boardId}")
    public Response delete(@PathVariable("boardId") Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        boardService.delete(boardId, member);
        return Response.success();
    }

    // 학생회
    @ApiOperation(value = "학생회 답변 등록", notes = "학생회 답변을 등록합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/{boardId}/replies")
    public Response createReply(@PathVariable("boardId") Long boardId, @Valid @RequestBody ReplyCreateRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        boardService.createReply(req, boardId, member);
        return Response.success();
    }

    @ApiOperation(value = "학생회 답변 조회", notes = "학생회 답변을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/{boardId}/replies")
    public Response findReply(@PathVariable("boardId") Long boardId) {
        return Response.success(boardService.findReply(boardId));
    }

    @ApiOperation(value = "학생회 답변 수정", notes = "학생회 답변을 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/{boardId}/replies")
    public Response editReply(@PathVariable("boardId") Long boardId, @Valid @RequestBody ReplyEditRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        boardService.editReply(boardId, req, member);
        return Response.success();
    }
}
