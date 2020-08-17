package com.example.sprinkle.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /** Standard Error */
    KP0001("처리중 오류가 발생하였습니다. 관리자에게 문의해주세요."),
    KP0002("잘못된 요청입니다."),

    /** Business Error */
    KP0101("진행중인 뿌리기가 너무 많습니다."),
    KP0102("인원수 보다 많은 금액을 입력하세요."),
    KP0103("너무 많이 뿌리면 안됩니다."),
    KP0104("인원수는 1명 이상이어야 합니다."),
    KP0201("자신은 받을 수 없습니다."),
    KP0202("마감되었습니다. 다음 기회에!"),
    KP0203("줍기 기회는 1번만!"),
    KP0204("늦었습니다. 다음 기회에!"),
    KP0301("조회 유효시간이 지났습니다."),
    ;

    private final String errorMessage;
}
