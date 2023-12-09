<template>
  <div id="main-container">
    <h2>Work Experiences</h2>
    <div v-for="(workExperience, index) in workExperienceData" v-bind:key="workExperience.id">
      <h3>{{ index + 1 }}. {{ workExperience.company }}</h3>
      <p>{{ workExperience.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';

defineComponent({
  name: 'WorkExperiencesSection'
});

let workExperienceData: Ref<{
  id: number,
  company: string,
  description: string,
  startDate: string,
  endDate: string,
  createdAt: Date,
  updatedAt: Date
}[]> = ref([]);

onBeforeMount(async () => {
  const response = await fetch("http://localhost:8081/v1/curriculum-vitae/work-experiences")
  console.log(response);
  
  if (response.ok) {
    workExperienceData.value = await response.json();
  }
});

</script>
