package br.com.torqueos.auth;

public final class CurrentUserContext {
  private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
  private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

  private CurrentUserContext(){}

  public static void set(Long userId, String role) {
    USER_ID.set(userId);
    ROLE.set(role);
  }

  public static Long getUserId() { return USER_ID.get(); }
  public static String getRole() { return ROLE.get(); }

  public static boolean isAdmin() {
    return "ADMIN".equalsIgnoreCase(getRole());
  }

  public static void clear() {
    USER_ID.remove();
    ROLE.remove();
  }
}
