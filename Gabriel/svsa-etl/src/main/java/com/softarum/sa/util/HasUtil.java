package com.softarum.sa.util;

import java.util.Collection;

public class HasUtil {

	public static final boolean content(String string) {
		return string != null && !string.trim().isEmpty();
	}

	public static final boolean content(Collection<?> collection) {
		if (collection == null || collection.size() == 0) {
			return false;
		}
		for (Object obj : collection) {
			if (HasUtil.content(obj)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean content(Object[] array) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (Object obj : array) {
			if (HasUtil.content(obj)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean content(String... array) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (String string : array) {
			if (HasUtil.content(string)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean content(Object object) {
		if (object instanceof String) {
			return content((String) object);
		} else if (object instanceof Collection) {
			return content((Collection<?>) object);
		} else if (object instanceof Object[]) {
			return content((Object[]) object);
		}
		return object != null;
	}

}