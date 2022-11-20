package com.jungwoo.apiserver.exception.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import com.jungwoo.apiserver.exception.validation.ValidationGroup.NotEmptyGroup;
import com.jungwoo.apiserver.exception.validation.ValidationGroup.SizeGroup;
import com.jungwoo.apiserver.exception.validation.ValidationGroup.PatternCheckGroup;
/**
 * fileName     : ValidationSequence
 * author       : jungwoo
 * description  :
 */
@GroupSequence({Default.class, NotEmptyGroup.class, SizeGroup.class, PatternCheckGroup.class, })
public interface ValidationSequence {
}
