<template>
  <div id="header-section">
    <h1>Daniel Falcón Ruiz</h1>
  </div>
  <hr>
  <WorkExperiencesSection/>
  <AcademicStudiesSection/>
</template>

<script>
import AcademicStudiesSection from './components/AcademicStudiesSection.vue';
import WorkExperiencesSection from './components/WorkExperiencesSection.vue'

export default {
  name: 'App',
  components: {
    WorkExperiencesSection,
    AcademicStudiesSection
}
}
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

h1 {
  margin: auto;
  font-size: 40px;
}

</style>
