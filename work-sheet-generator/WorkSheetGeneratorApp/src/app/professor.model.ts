/**
 * Represents a professor.
 */
export class Professor {

  /**
   * Creates a new Professor object.
   * @param id The unique identifier of the professor.
   * @param fullName The name of the professor.
   * @param courses An array of courses belonging to the professor.
   * @param email The email of the professor.
   * @param rank The academic rank of the professor.
   */
  constructor(
    public id?: number,
    public fullName?: string,
    public courses?: string[],
    public email?: string,
    public rank?: ProfessorRank
  ){}
}
/**
 * Represents the rank of a professor.
 */
 export enum ProfessorRank {
    Assistant = 'Asistent',
    Professor = 'Profesor',
 }
