export interface WorkExperience {
  id: number,
  company: string,
  description: string,
  startDate: string,
  endDate: string,
  createdAt: Date,
  updatedAt: Date
}

export interface AcademicStudy {
  id: number,
  schoolName: string,
  titleName: string,
  createdAt: Date,
  updatedAt: Date
}

export interface GenericInfo {
  id: number,
  aboutMe: string
}

