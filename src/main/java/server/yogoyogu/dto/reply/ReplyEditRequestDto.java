package server.yogoyogu.dto.reply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "학생회 답변 수정")
public class ReplyEditRequestDto {

    @ApiModelProperty(value = "수정할 내용", notes = "수정할 내용을 입력해주세요", required = true, example = "학생회는 이렇게 답변합니다. ~~")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}