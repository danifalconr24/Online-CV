<template>
  <div class="cv-container">

    <header class="cv-header">
      <div class="cv-header__content">
        <div>
          <p class="cv-header__label">Curriculum Vitae</p>
          <h1 class="cv-header__name">Daniel Falcón Ruiz</h1>
        </div>
        <div class="cv-header__right">
          <div class="cv-header__photo-wrap">
            <q-img src="../assets/profile2.jpeg" ratio="1" />
          </div>
          <q-btn
            v-if="!isAuthenticated"
            flat
            icon="lock"
            label="Login"
            class="cv-login-btn"
            @click="router.push('/login')"
          />
          <q-btn
            v-else
            flat
            icon="logout"
            label="Logout"
            class="cv-login-btn"
            @click="logout"
          />
        </div>
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

  </div>
</template>

<script setup lang="ts">
import { Ref, defineComponent, onBeforeMount, ref } from 'vue';
import { useRouter } from 'vue-router';
import AcademicStudiesSection from '../components/AcademicStudiesSection.vue';
import WorkExperiencesSection from '../components/WorkExperiencesSection.vue';
import { GenericInfo } from 'src/components/types/models';
import { useAuth } from '../composables/useAuth';

defineComponent({
  name: 'MainPage',
  components: {
    WorkExperiencesSection,
    AcademicStudiesSection,
  },
});

const router = useRouter();
const { isAuthenticated, logout, authHeaders } = useAuth();

let genericInfoData: Ref<GenericInfo> = ref({} as GenericInfo);
const editingAboutMe = ref(false);
const aboutMeDraft = ref('');
const savingAboutMe = ref(false);

onBeforeMount(async () => {
  const response = await fetch('http://localhost:8080/v1/curriculum-vitae/generic-info');
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
    `http://localhost:8080/v1/curriculum-vitae/generic-info/${genericInfoData.value.id}`,
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
</script>
