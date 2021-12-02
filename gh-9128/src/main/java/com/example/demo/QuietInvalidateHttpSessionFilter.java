package com.example.demo;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.springframework.web.filter.OncePerRequestFilter;

public class QuietInvalidateHttpSessionFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		chain.doFilter(new HttpServletRequestWrapper(request) {
			@Override
			public HttpSession getSession(boolean create) {
				HttpSession session = super.getSession(create);
				if (session == null) {
					return session;
				}
				return new QuietInvalidateHttpSessionWrapper(session);
			}
		}, response);
	}

	static final class QuietInvalidateHttpSessionWrapper implements HttpSession {
		private final HttpSession delegate;

		public QuietInvalidateHttpSessionWrapper(HttpSession delegate) {
			this.delegate = delegate;
		}

		@Override
		public long getCreationTime() {
			return delegate.getCreationTime();
		}

		@Override
		public String getId() {
			return delegate.getId();
		}

		@Override
		public long getLastAccessedTime() {
			return delegate.getLastAccessedTime();
		}

		@Override
		public ServletContext getServletContext() {
			return delegate.getServletContext();
		}

		@Override
		public void setMaxInactiveInterval(int i) {
			delegate.setMaxInactiveInterval(i);
		}

		@Override
		public int getMaxInactiveInterval() {
			return delegate.getMaxInactiveInterval();
		}

		@Override
		@Deprecated
		public HttpSessionContext getSessionContext() {
			return delegate.getSessionContext();
		}

		@Override
		public Object getAttribute(String s) {
			return delegate.getAttribute(s);
		}

		@Override
		@Deprecated
		public Object getValue(String s) {
			return delegate.getValue(s);
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return delegate.getAttributeNames();
		}

		@Override
		@Deprecated
		public String[] getValueNames() {
			return delegate.getValueNames();
		}

		@Override
		public void setAttribute(String s, Object o) {
			delegate.setAttribute(s, o);
		}

		@Override
		@Deprecated
		public void putValue(String s, Object o) {
			delegate.putValue(s, o);
		}

		@Override
		public void removeAttribute(String s) {
			delegate.removeAttribute(s);
		}

		@Override
		@Deprecated
		public void removeValue(String s) {
			delegate.removeValue(s);
		}

		@Override
		public void invalidate() {
			try {
				delegate.invalidate();
			} catch (IllegalStateException ex) {
				// ignoring
			}
		}

		@Override
		public boolean isNew() {
			return delegate.isNew();
		}
	}
}
