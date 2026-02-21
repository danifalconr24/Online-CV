import { computed, ref } from 'vue';

const TOKEN_KEY = 'cv_auth_token';

const token = ref<string | null>(localStorage.getItem(TOKEN_KEY));

export function useAuth() {
  const isAuthenticated = computed(() => token.value !== null);

  function setToken(newToken: string) {
    token.value = newToken;
    localStorage.setItem(TOKEN_KEY, newToken);
  }

  function clearToken() {
    token.value = null;
    localStorage.removeItem(TOKEN_KEY);
  }

  async function login(username: string, password: string): Promise<boolean> {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (response.ok) {
      const data = await response.json();
      setToken(data.accessToken);
      return true;
    }
    return false;
  }

  function logout() {
    clearToken();
  }

  function authHeaders(): HeadersInit {
    if (!token.value) return {};
    return { Authorization: `Bearer ${token.value}` };
  }

  return { isAuthenticated, login, logout, authHeaders };
}
