package jrainroot.still_home.dto;

import java.time.LocalDateTime;

import jrainroot.still_home.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    private long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public MemberDto(Member member) {
      this.id = member.getId();
      this.name = member.getName();
      this.createdDate = member.getCreateDate();
      this.updatedDate = member.getUpdateDate();
    }
}
