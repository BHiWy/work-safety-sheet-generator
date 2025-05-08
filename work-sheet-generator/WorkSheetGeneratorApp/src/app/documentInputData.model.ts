import {Group} from './group.model';

/**
 * Represents the input data required for generating a document.
 */
export class DocumentInputData {

  /**
   * Creates a new instance of the input data required for generating a document.
   * @param professorName The name of the professor.
   * @param courseName The name of the course.
   * @param assistantName The name of the assistant.
   * @param place The place where the course is taken
   * @param groups The list of groups.
   */
  constructor(
    public professorName: string,
    public courseName: string,
    public assistantName: string,
    public place: string,
    public groups: Group[],
  ) {
  }
}
