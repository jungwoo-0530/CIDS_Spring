package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.dto.maria.member.MemberPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * fileName     : MemberRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface MemberRepositoryCustom {


  Page<MemberPageDto> findAllPageSort(Pageable pageable);

  Page<MemberPageDto> findAllPageSortBySearch(Pageable pageable, String searchWord);
}
