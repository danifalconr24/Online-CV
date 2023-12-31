<template>
  <div id="main-container">
    <h2 class="text-h3">Work Experiences</h2>
    <div v-for="(workExperience, index) in workExperienceData" v-bind:key="workExperience.id">
      <h4>{{ index + 1 }}. {{ workExperience.company }}</h4>
      <p>{{ workExperience.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { WorkExperience } from './types/models';

defineComponent({
  name: 'WorkExperiencesSection'
});

let workExperienceData: Ref<WorkExperience[]> = ref([]);

onBeforeMount(async () => {
  const response = await fetch('http://localhost:8080/v1/curriculum-vitae/work-experiences')
  console.log(response);

  if (response.ok) {
    workExperienceData.value = await response.json();
  }
});

</script>
./models./models./models