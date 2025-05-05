/**
 * Represents a student.
 */
export class Student {

  /**
   * Creates a new Student object.
   * @param {string} [id] - The unique identifier of the student.
   * @param {string} [firstName] - The first name of the student.
   * @param {string} [lastName] - The last name of the student.
   * @param {number} [year] - The academic year of the student.
   * @param {string} [email] - The email address of the student.
   * @param {string} [paternalInitial] - The initial of the student's paternal name.
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
