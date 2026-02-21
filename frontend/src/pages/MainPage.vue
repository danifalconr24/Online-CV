<template>
  <div class="cv-container">

    <header class="cv-header">
      <div class="cv-header__content">
        <div>
          <p class="cv-header__label">Curriculum Vitae</p>
          <h1 class="cv-header__name">Daniel Falcón Ruiz</h1>
        </div>
        <div class="cv-header__photo-wrap">
          <q-img src="../assets/profile2.jpeg" ratio="1" />
        </div>
      </div>
    </header>

    <section class="cv-section" v-if="genericInfoData.aboutMe">
      <span class="cv-section__tag">About</span>
      <h2 class="cv-section__title">About Me</h2>
      <p class="cv-about__text">{{ genericInfoData.aboutMe }}</p>
    </section>

    <WorkExperiencesSection />
    <AcademicStudiesSection />

  </div>
</template>

<script setup lang="ts">
import { Ref, defineComponent, onBeforeMount, ref } from 'vue';
import AcademicStudiesSection from '../components/AcademicStudiesSection.vue';
import WorkExperiencesSection from '../components/WorkExperiencesSection.vue';
import { GenericInfo } from 'src/components/types/models';

defineComponent({
  name: 'MainPage',
  components: {
    WorkExperiencesSection,
    AcademicStudiesSection,
  },
});

let genericInfoData: Ref<GenericInfo> = ref({} as GenericInfo);

onBeforeMount(async () => {
  const response = await fetch('http://localhost:8080/v1/curriculum-vitae/generic-info');
  if (response.ok) {
    genericInfoData.value = await response.json();
  }
});
</script>
