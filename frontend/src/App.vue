<template>
  <div id="header-section">
    <h1>Daniel Falcón Ruiz</h1>
  </div>
  <hr>

  <div id="content-section">
    <WorkExperiencesSection />
    <AcademicStudiesSection />
  </div>
</template>

<script setup lang="ts">
import { defineComponent } from 'vue';
import AcademicStudiesSection from './components/AcademicStudiesSection.vue';
import WorkExperiencesSection from './components/WorkExperiencesSection.vue'

defineComponent({
  name: 'App',
  components: {
    WorkExperiencesSection,
    AcademicStudiesSection
  }
})
</script>

<style>
body {
  margin: 0;
}

hr {
  margin: 0;
}

#header-section {
  background-color: crimson;
  width: 100%;
  height: 50px;
  padding: 2%;
}

#content-section {
  padding: 2%;
}

h1 {
  margin: auto;
  font-size: 40px;
}
</style>
