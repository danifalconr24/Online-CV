<template>
    <h2>Academic Studies</h2>
    <h3>1. {{ academicStudiesData.at(0).schoolName }}</h3>
</template>

<script setup>

import { onBeforeMount, ref } from 'vue';

let academicStudiesData = ref(Array.of({
    id: Number,
    schoolName: String,
    titleName: String,
    createdAt: Date,
    updatedAt: Date
}));

onBeforeMount(async () => {
    const response = await fetch("http://localhost:8080/v1/curriculum-vitae/academic-studies")
    console.log(response);
    academicStudiesData.value = await response.json();
});

</script>