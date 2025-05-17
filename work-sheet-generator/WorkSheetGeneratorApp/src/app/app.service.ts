import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Student} from './student.model';
import {Group} from './group.model';
import {environment} from '../environments/environment';
import {DocumentInputData} from './documentInputData.model';
import {Professor, ProfessorRank} from './professor.model';

@Injectable({ providedIn:'root' })
export class AppService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  /**
   * Retrieves a list of students belonging to a specific group code.
   *
   * @param groupCode - The code of the group to find students for.
   * @returns {@link Observable} <{@link HttpResponse}<{@link Student[]}>> that emits an HTTP response
   * containing an array of Student objects.
   */
  public findStudentsByGroupCode(groupCode: string): Observable<HttpResponse<Student[]>> {
    return this.http.get<Student[]>(`${this.apiServerUrl}/group/find-students/${groupCode}`, {observe: "response"})
  }

  /**
   * Retrieves all groups.
   *
   * @returns {@link Observable} <{@link HttpResponse}<{@link Group[]}>> that emits an HTTP response
   * * containing an array of Group objects.
   */
  public getAllGroups(): Observable<HttpResponse<Group[]>>{
    return this.http.get<Group[]>(`${this.apiServerUrl}/group`, {observe: "response"})
  }

  /**
   * Retrieves all professors filtered by their rank.
   *
   * @param rank The rank of the professors to retrieve.
   * @returns {@link Observable} <{@link HttpResponse}<{@link Group[]}>> that will emit an HTTP response
   * containing an array of Professor objects matching the given rank.
   */
  public getProfessorsByRank(rank: ProfessorRank): Observable<HttpResponse<Professor[]>>{
    return this.http.get<Professor[]>(`${this.apiServerUrl}/professor/${rank}`, {observe: "response"})
  }

  /**
   * Retrieves the list of courses taught by a specific professor.
   *
   * @param id The unique identifier of the professor.
   * @returns {@link Observable} <{@link HttpResponse}<{@link string[]}>> that will emit an HTTP response
   * containing an array of strings, where each string is the name of a course taught by the professor.
   */
  public getCoursesByProfessorId(id: number): Observable<HttpResponse<string[]>>{
    return this.http.get<string[]>(`${this.apiServerUrl}/professor/get-courses/${id}`, {observe: "response"})
  }

  /**
   * Generates and retrieves a Word document from the backend API.
   *
   * @param input An object containing the data needed to generate the document.
   * @returns {@link Observable} <{@link Blob}> that will emit a Blob representing the generated Word document.
   */
  public getDocument(input: DocumentInputData): Observable<Blob>{
    return this.http.post(`${this.apiServerUrl}/word`, input, { responseType : "blob"})
  }
}
