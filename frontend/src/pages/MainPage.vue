<template>
  <div class="cv-container">

    <div class="cv-floating-header" :class="{ 'cv-floating-header--visible': showFloatingHeader }">
      <div class="cv-floating-header__photo">
        <q-img :src="profileImageSrc" ratio="1" />
      </div>
      <div class="cv-floating-header__text">
        <span class="cv-floating-header__name">Daniel Falcón Ruiz</span>
        <span class="cv-floating-header__subtitle">Java Software Engineer</span>
      </div>
    </div>

    <header ref="headerRef" class="cv-header">
      <div
        class="cv-header__photo-wrap"
        :class="{ 'cv-header__photo-wrap--editable': isAuthenticated }"
        @click="isAuthenticated && (showImageUpload = true)"
      >
        <q-img
          :src="profileImageSrc"
          ratio="1"
        />
        <div v-if="isAuthenticated" class="cv-photo-overlay">
          <q-icon name="edit" size="28px" color="white" />
        </div>
      </div>
      <div class="cv-header__content">
        <p class="cv-header__label">Curriculum Vitae</p>
        <h1 class="cv-header__name">Daniel Falcón Ruiz</h1>
        <p class="cv-header__subtitle">Java Software Engineer</p>
        <q-btn
          v-if="!isAuthenticated && showLoginButton"
          flat
          icon="lock"
          label="Login"
          class="cv-login-btn"
          @click="router.push('/login')"
        />
        <q-btn
          v-else-if="isAuthenticated"
          flat
          icon="logout"
          label="Logout"
          class="cv-login-btn"
          @click="() => { logout(); router.replace('/'); }"
        />
      </div>
    </header>

    <section class="cv-section" v-if="genericInfoData.aboutMe">
      <div class="cv-section__header">
        <span class="cv-section__tag">About</span>
        <q-btn
          v-if="isAuthenticated && !editingAboutMe"
          flat
          round
          icon="edit"
          size="sm"
          class="cv-edit-btn"
          @click="startEditAboutMe"
        />
      </div>
      <h2 class="cv-section__title">About Me</h2>
      <div v-if="editingAboutMe" class="cv-about__edit">
        <q-input
          v-model="aboutMeDraft"
          type="textarea"
          outlined
          autogrow
          class="cv-about__textarea"
        />
        <div class="cv-edit__actions">
          <q-btn flat label="Cancel" @click="cancelEditAboutMe" />
          <q-btn unelevated color="primary" label="Save" :loading="savingAboutMe" @click="saveAboutMe" />
        </div>
      </div>
      <p v-else class="cv-about__text">{{ genericInfoData.aboutMe }}</p>
    </section>

    <WorkExperiencesSection :is-authenticated="isAuthenticated" :auth-headers="authHeaders" />
    <AcademicStudiesSection :is-authenticated="isAuthenticated" :auth-headers="authHeaders" />

    <q-dialog v-model="showImageUpload">
      <q-card style="min-width: 360px">
        <q-card-section>
          <div class="text-h6">Update Profile Image</div>
        </q-card-section>
        <q-card-section>
          <input
            type="file"
            accept="image/*"
            @change="onFileSelect"
            ref="fileInput"
          />
          <div v-if="imagePreview" class="cv-image-preview">
            <img :src="imagePreview" alt="Preview" />
          </div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Cancel" @click="showImageUpload = false" />
          <q-btn
            unelevated
            color="primary"
            label="Save"
            :loading="uploadingImage"
            :disable="!selectedFile"
            @click="uploadImage"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>

  </div>
</template>

<script setup lang="ts">
import {
  Ref,
  computed,
  defineComponent,
  onBeforeMount,
  onMounted,
  onUnmounted,
  ref,
} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AcademicStudiesSection from '../components/AcademicStudiesSection.vue';
import WorkExperiencesSection from '../components/WorkExperiencesSection.vue';
import { GenericInfo } from 'src/components/types/models';
import { useAuth } from '../composables/useAuth';
import { API_ENDPOINTS } from '../config/api';
import profileFallback from '../assets/profile2.jpeg';

defineComponent({
  name: 'MainPage',
  components: {
    WorkExperiencesSection,
    AcademicStudiesSection,
  },
});

const router = useRouter();
const route = useRoute();
const { isAuthenticated, logout, authHeaders } = useAuth();

const showLoginButton = computed(() => route.query.isAdmin === 'true');

const headerRef = ref<HTMLElement | null>(null);
const showFloatingHeader = ref(false);
let headerObserver: IntersectionObserver | null = null;

let genericInfoData: Ref<GenericInfo> = ref({} as GenericInfo);

const profileImageSrc = computed(() =>
  genericInfoData.value.profileImage
    ? `data:image/*;base64,${genericInfoData.value.profileImage}`
    : profileFallback,
);
const editingAboutMe = ref(false);
const aboutMeDraft = ref('');
const savingAboutMe = ref(false);

const showImageUpload = ref(false);
const selectedFile = ref<File | null>(null);
const imagePreview = ref<string | null>(null);
const uploadingImage = ref(false);

onMounted(() => {
  if (headerRef.value) {
    headerObserver = new IntersectionObserver(
      ([entry]) => {
        showFloatingHeader.value = !entry.isIntersecting;
      },
      { threshold: 0 },
    );
    headerObserver.observe(headerRef.value);
  }
});

onUnmounted(() => {
  headerObserver?.disconnect();
});

onBeforeMount(async () => {
  const response = await fetch(API_ENDPOINTS.genericInfo);
  if (response.ok) {
    genericInfoData.value = await response.json();
  }
});

function startEditAboutMe() {
  aboutMeDraft.value = genericInfoData.value.aboutMe;
  editingAboutMe.value = true;
}

function cancelEditAboutMe() {
  editingAboutMe.value = false;
}

async function saveAboutMe() {
  savingAboutMe.value = true;
  const response = await fetch(
    `${API_ENDPOINTS.genericInfo}/${genericInfoData.value.id}`,
    {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', ...authHeaders() },
      body: JSON.stringify({ aboutMe: aboutMeDraft.value }),
    }
  );
  if (response.ok) {
    genericInfoData.value = await response.json();
    editingAboutMe.value = false;
  }
  savingAboutMe.value = false;
}

function onFileSelect(event: Event) {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0] ?? null;
  selectedFile.value = file;
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  } else {
    imagePreview.value = null;
  }
}

async function uploadImage() {
  if (!selectedFile.value) return;
  uploadingImage.value = true;
  const formData = new FormData();
  formData.append('file', selectedFile.value);
  const response = await fetch(
    `${API_ENDPOINTS.genericInfo}/${genericInfoData.value.id}/image`,
    {
      method: 'PUT',
      headers: { ...authHeaders() },
      body: formData,
    }
  );
  if (response.ok) {
    genericInfoData.value = await response.json();
    showImageUpload.value = false;
    selectedFile.value = null;
    imagePreview.value = null;
  }
  uploadingImage.value = false;
}
</script>
