<template>
    <h2>Academic Studies</h2>
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
    const response = await fetch("http://localhost:8080/v1/curriculum-vitae/academic-studies")
    console.log(response);
    academicStudiesData.value = await response.json();
});

</script>