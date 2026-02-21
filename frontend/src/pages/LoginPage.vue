<template>
  <div class="login-container">
    <q-card class="login-card">
      <q-card-section>
        <h2 class="login-title">Admin Login</h2>
      </q-card-section>

      <q-card-section>
        <q-form @submit.prevent="handleLogin">
          <q-input
            v-model="username"
            label="Username"
            outlined
            class="login-input"
            :disable="loading"
          />
          <q-input
            v-model="password"
            label="Password"
            type="password"
            outlined
            class="login-input"
            :disable="loading"
          />
          <p v-if="errorMessage" class="login-error">{{ errorMessage }}</p>
          <q-btn
            type="submit"
            label="Login"
            color="primary"
            class="login-btn"
            :loading="loading"
            unelevated
          />
        </q-form>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '../composables/useAuth';

const router = useRouter();
const { login } = useAuth();

const username = ref('');
const password = ref('');
const loading = ref(false);
const errorMessage = ref('');

async function handleLogin() {
  loading.value = true;
  errorMessage.value = '';
  const success = await login(username.value, password.value);
  loading.value = false;
  if (success) {
    await router.push('/');
  } else {
    errorMessage.value = 'Invalid username or password.';
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f5f5f5;
}

.login-card {
  width: 100%;
  max-width: 400px;
  border-radius: 12px;
}

.login-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: #1e1b4b;
}

.login-input {
  margin-bottom: 16px;
}

.login-error {
  color: #dc2626;
  font-size: 0.875rem;
  margin-bottom: 12px;
}

.login-btn {
  width: 100%;
}
</style>
