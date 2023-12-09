<template>
  <div id="header-section">
    <h1>Daniel Falcón Ruiz</h1>
  </div>
  <hr>
  
  <div id="aboutme-section">
    <h2>About me</h2>
    <pre>
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris at mi quis mauris egestas tempus.
      Curabitur commodo, nisi eu hendrerit viverra, diam nisl egestas dolor, ac blandit est erat eu eros.

      Morbi auctor gravida lorem, eget vulputate eros facilisis auctor. Vestibulum maximus lectus velit,
      quis iaculis ex fringilla sed. Vestibulum placerat vel odio in efficitur. Etiam tempor tempus lacus. 

      Suspendisse in dapibus arcu, ac commodo quam. Vivamus pharetra neque eu rutrum congue. Nullam laoreet 
      tincidunt ipsum varius consectetur.
      
      Mauris at magna pharetra, suscipit odio at, suscipit lacus. 
      
      Aliquam id tellus augue. Mauris ut enim purus. Proin ac sem ut leo aliquam rutrum vel quis elit. Donec et euismod 
      velit. Vestibulum molestie posuere erat. Duis orci metus, efficitur nec finibus sed, porttitor ac nibh. 
      In rutrum tristique pharetra. Fusce molestie sem id massa rhoncus, eget feugiat odio porttitor.
    </pre>
  </div>
  
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

#aboutme-section {
  padding: 2%;
}

h1 {
  margin: auto;
  font-size: 40px;
}
</style>
