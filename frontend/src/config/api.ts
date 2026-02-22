const API_URL = process.env.API_URL || 'http://localhost:8080';

export const API_BASE_URL = API_URL;

export const API_ENDPOINTS = {
  workExperiences: `${API_URL}/v1/curriculum-vitae/work-experiences`,
  academicStudies: `${API_URL}/v1/curriculum-vitae/academic-studies`,
  genericInfo: `${API_URL}/v1/curriculum-vitae/generic-info`,
  auth: {
    login: `${API_URL}/auth/login`,
    refresh: `${API_URL}/auth/refresh`,
  },
} as const;
