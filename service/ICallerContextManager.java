package com.bh.realtrack.service;

import javax.servlet.http.HttpServletRequest;
import com.bh.realtrack.exception.UnknowAuthenticationException;

import com.bh.realtrack.model.CallerContext;

public interface ICallerContextManager {

	//CallerContext getCallerContext(HttpServletRequest request);
    CallerContext getCallerContext(HttpServletRequest request)throws UnknowAuthenticationException;

}
