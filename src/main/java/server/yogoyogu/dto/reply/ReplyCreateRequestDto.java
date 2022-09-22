package server.yogoyogu.dto.reply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "학생회 답변 작성")
public class ReplyCreateRequestDto {

    @ApiModelProperty(value = "내용", notes = "내용을 입력해주세요", required = true, example = "학생회는 이렇게 답변합니다. ~~")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
