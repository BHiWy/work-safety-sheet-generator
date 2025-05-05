import {Student} from './student.model';
/**
 * Represents a group of students.
 */
export class Group {

  /**
   * Creates a new Group object.
   * @param {string} [id] - The unique identifier of the group.
   * @param {string} [code] - The code or name of the group.
   * @param {number} [year] - The academic year the group belongs to.
   * @param {Student[]} [students] - An array of students belonging to this group.
   * @param {Student} [groupLeader] - The student who is the leader of this group.
   */
  constructor(
    public id?: string,
    public code?: string,
    public year?: number,
    public students?: Student[],
    public groupLeader?: Student
  ){}
}
