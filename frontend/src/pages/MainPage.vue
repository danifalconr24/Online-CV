<template>
    <div>
        <div id="header-section" class="q-px-xl row bg-red-10">
            <div class="col-7">
                <h1 class="text-h1">Daniel Falcón Ruiz</h1>
            </div>
            <div class="col-2 offset-3 q-pa-md text-center">
                <q-img src="../assets/profile2.jpeg" ratio="1" width="190px" height=""
                    :img-style="{ borderRadius: '50%' }" />
            </div>
        </div>

        <div id="aboutme-section" class="q-px-xl">
            <h2 class="text-h3">About me</h2>
            <p class="q-px-sm">
                {{ genericInfoData.aboutMe }}
            </p>
        </div>

        <div id="content-section" class="q-px-xl ">
            <WorkExperiencesSection />
            <AcademicStudiesSection />
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { Ref, defineComponent, onBeforeMount, ref } from 'vue';
import AcademicStudiesSection from '../components/AcademicStudiesSection.vue';
import WorkExperiencesSection from '../components/WorkExperiencesSection.vue'
import { GenericInfo } from 'src/components/types/models';

defineComponent({
    name: 'MainPage',
    components: {
        WorkExperiencesSection,
        AcademicStudiesSection
    }
});

let genericInfoData: Ref<GenericInfo> = ref({} as GenericInfo);

onBeforeMount(async () => {
    const response = await fetch('http://localhost:8080/v1/curriculum-vitae/generic-info')
    console.log(response);

    if (response.ok) {
        genericInfoData.value = await response.json();
    }
});

</script>
  