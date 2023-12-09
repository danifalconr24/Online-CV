<template>
    <h2>Academic Studies</h2>
    <div v-for="(academicStudy, index) in academicStudiesData" v-bind:key="academicStudy.id">
        <span><b>{{ index + 1 }}. {{ academicStudy.titleName }}: </b></span><span>{{ academicStudy.schoolName }}</span>
    </div>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';

defineComponent({
    name: 'AcademicStudiesSection'
})

let academicStudiesData: Ref<{
    id: number,
    schoolName: string,
    titleName: string,
    createdAt: Date,
    updatedAt: Date
}[]> = ref([]);

onBeforeMount(async () => {
    const response = await fetch("http://localhost:8081/v1/curriculum-vitae/academic-studies")
    console.log(response);

    if (response.ok) {
        academicStudiesData.value = await response.json();
    }
});

</script>