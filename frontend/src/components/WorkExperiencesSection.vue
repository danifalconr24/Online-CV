<template>
  <div :v-if="workExperienceData.length" v-for="(workExperience, index) in workExperienceData" v-bind:key="workExperience.id">
    <h2>Work Experiences</h2>
    <h3>{{ index + 1 }}. {{ workExperience.company }}</h3>
    <p>{{ workExperience.description }}</p>
  </div>
</template>

<script setup>

import { onBeforeMount, ref } from 'vue';

let workExperienceData = ref(Array.of({
  id: Number,
  company: String,
  description: String,
  startDate: String,
  endDate: String,
  createdAt: Date,
  updatedAt: Date
}));

onBeforeMount(async () => {
  const response = await fetch("http://localhost:8080/v1/curriculum-vitae/work-experiences")
  console.log(response);
  workExperienceData.value = await response.json();
});

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
