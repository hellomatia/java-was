package codesquad.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final long sessionTimeout; // in milliseconds

    public SessionManager(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Session createSession() {
        Session session = new Session();
        sessions.put(session.getId(), session);
        return session;
    }

    public Session getSession(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session != null) {
            if (isExpired(session)) {
                invalidateSession(sessionId);
                return null;
            }
            session.access();
        }
        return session;
    }

    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    private boolean isExpired(Session session) {
        return System.currentTimeMillis() - session.getLastAccessedTime() > sessionTimeout;
    }

    public void cleanExpiredSessions() {
        sessions.entrySet().removeIf(entry -> isExpired(entry.getValue()));
    }
}
