<template>
  <section class="cv-section">
    <span class="cv-section__tag">Education</span>
    <h2 class="cv-section__title">Academic Studies</h2>
    <div class="cv-academic-grid">
      <div
        v-for="academicStudy in academicStudiesData"
        :key="academicStudy.id"
        class="cv-academic-card"
      >
        <p class="cv-academic__icon">🎓</p>
        <p class="cv-academic__degree">{{ academicStudy.titleName }}</p>
        <p class="cv-academic__school">{{ academicStudy.schoolName }}</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { AcademicStudy } from './types/models';

defineComponent({
  name: 'AcademicStudiesSection',
});

let academicStudiesData: Ref<AcademicStudy[]> = ref([]);

onBeforeMount(async () => {
  const response = await fetch('http://localhost:8080/v1/curriculum-vitae/academic-studies');
  if (response.ok) {
    academicStudiesData.value = await response.json();
  }
});
</script>
