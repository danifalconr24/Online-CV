<template>
  <section class="cv-section">
    <div class="cv-section__header">
      <span class="cv-section__tag">Experience</span>
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
    <h2 class="cv-section__title">Work Experience</h2>

    <div v-if="creatingNew" class="cv-experience-item cv-experience-item--edit">
      <q-input v-model="newDraft.company" label="Company" outlined dense class="cv-edit-field" />
      <div class="cv-edit-row">
        <q-input v-model="newDraft.startDate" label="Start date (DD/MM/YYYY)" outlined dense class="cv-edit-field" />
        <q-input v-model="newDraft.endDate" label="End date (DD/MM/YYYY, blank = present)" outlined dense class="cv-edit-field" />
      </div>
      <q-input v-model="newDraft.description" label="Description" type="textarea" autogrow outlined dense class="cv-edit-field" />
      <div class="cv-edit__actions">
        <q-btn flat label="Cancel" @click="cancelCreate" />
        <q-btn unelevated color="primary" label="Create" :loading="saving" @click="createExperience" />
      </div>
    </div>

    <div class="cv-experience-list">
      <div
        v-for="workExperience in workExperienceData"
        :key="workExperience.id"
        class="cv-experience-item"
      >
        <template v-if="editingId === workExperience.id">
          <q-input v-model="editDraft.company" label="Company" outlined dense class="cv-edit-field" />
          <div class="cv-edit-row">
            <q-input v-model="editDraft.startDate" label="Start date (DD/MM/YYYY)" outlined dense class="cv-edit-field" />
            <q-input v-model="editDraft.endDate" label="End date (DD/MM/YYYY, blank = present)" outlined dense class="cv-edit-field" />
          </div>
          <q-input v-model="editDraft.description" label="Description" type="textarea" autogrow outlined dense class="cv-edit-field" />
          <div class="cv-edit__actions">
            <q-btn flat label="Cancel" @click="cancelEdit" />
            <q-btn unelevated color="primary" label="Save" :loading="saving" @click="saveEdit(workExperience.id)" />
          </div>
        </template>
        <template v-else>
          <div class="cv-experience__row">
            <p class="cv-experience__company">{{ workExperience.company }}</p>
            <div v-if="isAuthenticated" class="cv-item-actions">
              <q-btn flat round icon="edit" size="xs" @click="startEdit(workExperience)" />
              <q-btn flat round icon="delete" size="xs" color="negative" @click="deleteExperience(workExperience.id)" />
            </div>
          </div>
          <p class="cv-experience__date">
            {{ formatDate(workExperience.startDate) }} &mdash;
            {{ workExperience.endDate ? formatDate(workExperience.endDate) : 'Present' }}
          </p>
          <p class="cv-experience__desc">{{ workExperience.description }}</p>
        </template>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineComponent, onBeforeMount, ref, type Ref } from 'vue';
import { WorkExperience } from './types/models';

defineComponent({
  name: 'WorkExperiencesSection',
});

const props = defineProps<{
  isAuthenticated: boolean;
  authHeaders: () => HeadersInit;
}>();

const BASE_URL = 'http://localhost:8080/v1/curriculum-vitae/work-experiences';

let workExperienceData: Ref<WorkExperience[]> = ref([]);
const editingId = ref<number | null>(null);
const editDraft = ref({ company: '', startDate: '', endDate: '', description: '' });
const creatingNew = ref(false);
const newDraft = ref({ company: '', startDate: '', endDate: '', description: '' });
const saving = ref(false);

onBeforeMount(async () => {
  await loadAll();
});

async function loadAll() {
  const response = await fetch(BASE_URL);
  if (response.ok) {
    workExperienceData.value = await response.json();
  }
}

function startEdit(item: WorkExperience) {
  editingId.value = item.id;
  editDraft.value = {
    company: item.company,
    startDate: item.startDate,
    endDate: item.endDate ?? '',
    description: item.description,
  };
}

function cancelEdit() {
  editingId.value = null;
}

async function saveEdit(id: number) {
  saving.value = true;
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', ...props.authHeaders() },
    body: JSON.stringify({
      company: editDraft.value.company,
      startDate: editDraft.value.startDate,
      endDate: editDraft.value.endDate || null,
      description: editDraft.value.description,
    }),
  });
  if (response.ok) {
    await loadAll();
    editingId.value = null;
  }
  saving.value = false;
}

async function deleteExperience(id: number) {
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: 'DELETE',
    headers: { ...props.authHeaders() },
  });
  if (response.ok) {
    workExperienceData.value = workExperienceData.value.filter((w) => w.id !== id);
  }
}

function startCreate() {
  newDraft.value = { company: '', startDate: '', endDate: '', description: '' };
  creatingNew.value = true;
}

function cancelCreate() {
  creatingNew.value = false;
}

async function createExperience() {
  saving.value = true;
  const response = await fetch(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...props.authHeaders() },
    body: JSON.stringify({
      company: newDraft.value.company,
      startDate: newDraft.value.startDate,
      endDate: newDraft.value.endDate || null,
      description: newDraft.value.description,
    }),
  });
  if (response.ok) {
    await loadAll();
    creatingNew.value = false;
  }
  saving.value = false;
}

function formatDate(dateStr: string): string {
  if (!dateStr) return '';
  // Backend stores dates as DD/MM/YYYY
  const [day, month, year] = dateStr.split('/').map(Number);
  const date = new Date(year, month - 1, day);
  if (isNaN(date.getTime())) return dateStr;
  return date.toLocaleDateString('en-US', { month: 'short', year: 'numeric' });
}
</script>
