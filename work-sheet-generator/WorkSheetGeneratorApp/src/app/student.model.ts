/**
 * Represents a student.
 */
export class Student {

  /**
   * Creates a new Student object.
   * @param id The unique identifier of the student.
   * @param firstName The first name of the student.
   * @param lastName The last name of the student.
   * @param year The academic year of the student.
   * @param email The email address of the student.
   * @param paternalInitial The initial of the student's paternal name.
   */
  constructor(
    public id?: string,
    public firstName?: string,
    public lastName?:string,
    public year?: number,
    public email?: string,
    public paternalInitial?: string
  ){}
}
