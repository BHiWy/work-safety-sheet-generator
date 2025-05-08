import {Group} from './group.model';

/**
 * Represents the input data required for generating a document.
 */
export class DocumentInputData {

  /**
   * Creates a new instance of the input data required for generating a document.
   * @param {string} professorName - The name of the professor.
   * @param {string} courseName - The name of the course.
   * @param {string} assistantName - The name of the assistant.
   * @param {Group[]} groups - The list of groups.
   */
  constructor(
    public professorName: string,
    public courseName: string,
    public assistantName: string,
    public groups: Group[],
  ) {
  }
}
