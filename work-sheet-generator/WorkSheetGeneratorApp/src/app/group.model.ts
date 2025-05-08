import {Student} from './student.model';
/**
 * Represents a group of students.
 */
export class Group {

  /**
   * Creates a new Group object.
   * @param id The unique identifier of the group.
   * @param code The code or name of the group.
   * @param year The academic year the group belongs to.
   * @param students An array of students belonging to this group.
   * @param groupLeader The student who is the leader of this group.
   */
  constructor(
    public id?: string,
    public code?: string,
    public year?: number,
    public students?: Student[],
    public groupLeader?: Student
  ){}
}
