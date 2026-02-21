<template>
  <section class="cv-section">
    <div class="cv-section__header">
      <span class="cv-section__tag">Education</span>
      <q-btn
        v-if="isAuthenticated"
        flat
        round
        icon="add"
        size="sm"
        class="cv-edit-btn"
        @click="startCreate"
      />
    </div>
    <h2 class="cv-section__title">Academic Studies</h2>

    <div v-if="creatingNew" class="cv-academic-card cv-academic-card--edit">
      <q-input v-model="newDraft.titleName" label="Degree / Title" outlined dense class="cv-edit-field" />
      <q-input v-model="newDraft.schoolName" label="School" outlined dense class="cv-edit-field" />
      <div class="cv-edit__actions">
        <q-btn flat label="Cancel" @click="cancelCreate" />
        <q-btn unelevated color="primary" label="Create" :loading="saving" @click="createStudy" />
      </div>
    </div>

    <div class="cv-academic-grid">
      <div
        v-for="academicStudy in academicStudiesData"
        :key="academicStudy.id"
        class="cv-academic-card"
      >
        <template v-if="editingId === academicStudy.id">
          <q-input v-model="editDraft.titleName" label="Degree / Title" outlined dense class="cv-edit-field" />
          <q-input v-model="editDraft.schoolName" label="School" outlined dense class="cv-edit-field" />
          <div class="cv-edit__actions">
            <q-btn flat label="Cancel" @click="cancelEdit" />
            <q-btn unelevated color="primary" label="Save" :loading="saving" @click="saveEdit(academicStudy.id)" />
          </div>
        </template>
        <template v-else>
          <div class="cv-academic__actions-row">
            <p class="cv-academic__icon">🎓</p>
            <div v-if="isAuthenticated" class="cv-item-actions">
              <q-btn flat round icon="edit" size="xs" @click="startEdit(academicStudy)" />
              <q-btn flat round icon="delete" size="xs" color="negative" @click="deleteStudy(academicStudy.id)" />
            </div>
          </div>
          <p class="cv-academic__degree">{{ academicStudy.titleName }}</p>
          <p class="cv-academic__school">{{ academicStudy.schoolName }}</p>
        </template>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { AcademicStudy } from './types/models';

defineComponent({
  name: 'AcademicStudiesSection',
});

const props = defineProps<{
  isAuthenticated: boolean;
  authHeaders: () => HeadersInit;
}>();

const BASE_URL = 'http://localhost:8080/v1/curriculum-vitae/academic-studies';

let academicStudiesData: Ref<AcademicStudy[]> = ref([]);
const editingId = ref<number | null>(null);
const editDraft = ref({ schoolName: '', titleName: '' });
const creatingNew = ref(false);
const newDraft = ref({ schoolName: '', titleName: '' });
const saving = ref(false);

onBeforeMount(async () => {
  await loadAll();
});

async function loadAll() {
  const response = await fetch(BASE_URL);
  if (response.ok) {
    academicStudiesData.value = await response.json();
  }
}

function startEdit(item: AcademicStudy) {
  editingId.value = item.id;
  editDraft.value = { schoolName: item.schoolName, titleName: item.titleName };
}

function cancelEdit() {
  editingId.value = null;
}

async function saveEdit(id: number) {
  saving.value = true;
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', ...props.authHeaders() },
    body: JSON.stringify({ schoolName: editDraft.value.schoolName, titleName: editDraft.value.titleName }),
  });
  if (response.ok) {
    await loadAll();
    editingId.value = null;
  }
  saving.value = false;
}

async function deleteStudy(id: number) {
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: 'DELETE',
    headers: { ...props.authHeaders() },
  });
  if (response.ok) {
    academicStudiesData.value = academicStudiesData.value.filter((s) => s.id !== id);
  }
}

function startCreate() {
  newDraft.value = { schoolName: '', titleName: '' };
  creatingNew.value = true;
}

function cancelCreate() {
  creatingNew.value = false;
}

async function createStudy() {
  saving.value = true;
  const response = await fetch(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...props.authHeaders() },
    body: JSON.stringify({ schoolName: newDraft.value.schoolName, titleName: newDraft.value.titleName }),
  });
  if (response.ok) {
    await loadAll();
    creatingNew.value = false;
  }
  saving.value = false;
}
</script>
