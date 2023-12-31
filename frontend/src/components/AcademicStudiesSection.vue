<template>
    <h2 class="text-h3">Academic Studies</h2>
    <div v-for="(academicStudy, index) in academicStudiesData" v-bind:key="academicStudy.id">
        <span class="text-h4">{{ index + 1 }}. {{ academicStudy.titleName }}: </span><span>{{ academicStudy.schoolName
        }}</span>
    </div>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { AcademicStudy } from './types/models';

defineComponent({
    name: 'AcademicStudiesSection'
})

let academicStudiesData: Ref<AcademicStudy[]> = ref([]);

onBeforeMount(async () => {
    const response = await fetch('http://localhost:8080/v1/curriculum-vitae/academic-studies')
    console.log(response);

    if (response.ok) {
        academicStudiesData.value = await response.json();
    }
});

</script>./types/models./models./models./models