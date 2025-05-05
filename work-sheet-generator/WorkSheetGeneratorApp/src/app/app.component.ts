import {Component, OnInit, ViewChild} from '@angular/core';
import {Group} from './group.model';
import {GroupService} from './group.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {FormsModule, NgForm} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [FormsModule, NgIf, NgForOf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'WorkSheetGeneratorApp';
  @ViewChild('form') form!: NgForm;

  public groups: Group[] = [];
  public selectedGroups: Group[] = [];
  public selectedGroupCodeToAdd: string | null = null;
  public errorMessages: string[] = [];

  constructor(private groupService: GroupService) {}

  ngOnInit() {
    this.getGroups();
  }

  /**
   * Retrieves all groups from the group service.
   * The retrieved groups are then assigned to the `this.groups` property.
   * In case of an error during the retrieval, an alert message displaying the error is shown.
   *
   * @public
   * @returns {void}
   */
  public getGroups(): void {
    this.groupService.getAll().subscribe({
       next: (response: HttpResponse<Group[]>) => {
         if (response && response.body){
           this.groups = response.body;
         }
       },
        error: (err:HttpErrorResponse) => {
         alert(err.message)
      }
    });
  }

  /**
   * Adds the selected group to the selected groups list if it exists and is not already present.
   * Resets the selected group code afterwards.
   *
   * @public
   * @returns {void}
   */
  public addGroup(): void {
    if (this.selectedGroupCodeToAdd) {
      const groupToAdd = this.groups.find(group => group.code === this.selectedGroupCodeToAdd);
      if (groupToAdd && !this.selectedGroups.some(group => group.code === groupToAdd.code)) {
        this.selectedGroups.push(groupToAdd);
        this.selectedGroupCodeToAdd = null;
      }
    }
  }

  /**
   * Removes a group from the selected groups list at the specified index.
   *
   * @public
   * @param {number} index - The index of the group to remove.
   * @returns {void}
   */
  public removeGroup(index: number): void {
    this.selectedGroups.splice(index, 1);
  }

  /**
   * Handles the form submission to generate a workSheetFile.
   * It performs form validation, displays error messages if the form is invalid or no groups are selected,
   * and logs the form data and selected groups to the console.
   * This function sends the data to a backend service.
   *
   * @public
   * @param {NgForm} form - The Angular form object.
   * @returns {void}
   */
  public generateWorksheetFile(form: NgForm): void {
    this.errorMessages = [];
    if (form.invalid) {
      if (form.controls['professorName']?.errors?.['required']) {
        this.errorMessages.push('Numele profesorului este obligatoriu.');
      }
      if (form.controls['courseName']?.errors?.['required']) {
        this.errorMessages.push('Denumirea cursului este obligatorie.');
      }
      if (form.controls['assistantName']?.errors?.['required']) {
        this.errorMessages.push('Numele profesorului assistent este obligatoriu.');
      }
      if (form.controls['place']?.errors?.['required']) {
        this.errorMessages.push('Locul desfășurării este obligatoriu.');
      }
      if (this.selectedGroups.length === 0 ) {
        this.errorMessages.push('Nu ați selectat nicio grupă.');
      }
      setTimeout(() => {
        this.errorMessages = [];
      }, 8000);
      return;
    }

    const formData = form.value;
    console.log('Datele formularului:', formData);
    console.log('Grupele selectate:', this.selectedGroups);
    // alert('Fișa ar fi generată acum.');
    // Aici poți trimite datele către backend pentru generarea fișei
  }

  /**
   * Resets the form and clears related data such as selected groups and error messages.
   *
   * @public
   * @returns {void}
   */
  public deleteInfo(): void {
      if (this.form) {
      this.form.resetForm();
    }
    this.selectedGroups = [];
    this.selectedGroupCodeToAdd = null;
    this.errorMessages = [];
  }
}
