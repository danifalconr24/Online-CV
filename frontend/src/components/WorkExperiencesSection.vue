<template>
  <section class="cv-section">
    <span class="cv-section__tag">Experience</span>
    <h2 class="cv-section__title">Work Experience</h2>
    <div class="cv-experience-list">
      <div v-for="workExperience in workExperienceData" :key="workExperience.id" class="cv-experience-item">
        <p class="cv-experience__company">{{ workExperience.company }}</p>
        <p class="cv-experience__date">
          {{ formatDate(workExperience.startDate) }} &mdash;
          {{ workExperience.endDate ? formatDate(workExperience.endDate) : 'Present' }}
        </p>
        <p class="cv-experience__desc">{{ workExperience.description }}</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { WorkExperience } from './types/models';

defineComponent({
  name: 'WorkExperiencesSection',
});

let workExperienceData: Ref<WorkExperience[]> = ref([]);

onBeforeMount(async () => {
  const response = await fetch('http://localhost:8080/v1/curriculum-vitae/work-experiences');
  if (response.ok) {
    workExperienceData.value = await response.json();
  }
});

function formatDate(dateStr: string): string {
  if (!dateStr) return '';
  // Backend stores dates as DD/MM/YYYY
  const [day, month, year] = dateStr.split('/').map(Number);
  const date = new Date(year, month - 1, day);
  if (isNaN(date.getTime())) return dateStr;
  return date.toLocaleDateString('en-US', { month: 'short', year: 'numeric' });
}
</script>
