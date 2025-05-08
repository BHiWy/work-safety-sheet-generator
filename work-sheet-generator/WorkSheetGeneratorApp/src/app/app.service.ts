import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Student} from './student.model';
import {Group} from './group.model';
import {environment} from '../environments/environment';
import {DocumentInputData} from './documentInputData.model';

@Injectable({ providedIn:'root' })
export class AppService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  /**
   * Retrieves a list of students belonging to a specific group code.
   *
   * @param groupCode - The code of the group to find students for.
   * @returns {Observable<HttpResponse<Student[]>>} An Observable that emits an HTTP response
   * containing an array of Student objects.
   */
  public findStudentsByGroupCode(groupCode: string): Observable<HttpResponse<Student[]>> {
    return this.http.get<Student[]>(`${this.apiServerUrl}/group/find-students/${groupCode}`, {observe: "response"})
  }

  /**
   * Retrieves all groups.
   *
   * @returns {Observable<HttpResponse<Group[]>>} An Observable that emits an HTTP response
   * containing an array of Group objects.
   */
  public getAll(): Observable<HttpResponse<Group[]>>{
    return this.http.get<Group[]>(`${this.apiServerUrl}/group`, {observe: "response"})
  }

  public getDocument(input: DocumentInputData): Observable<Blob>{
    return this.http.post(`${this.apiServerUrl}/word`, input, { responseType : "blob"})
  }
}
