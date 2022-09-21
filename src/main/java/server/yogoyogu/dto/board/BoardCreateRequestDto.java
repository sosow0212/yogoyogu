package server.yogoyogu.dto.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "게시글 생성 요청")
public class BoardCreateRequestDto {

    @ApiModelProperty(value = "제목", notes = "제목을 입력해주세요", required = true, example = "학생회에게 건의합니다.")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "본문", notes = "본문을 입력해주세요", required = true, example = "교내 주차 공간 좀 늘려주세요!")
    @NotBlank(message = "본문을 입력해주세요.")
    private String content;
}
