import {Component, OnInit, ViewChild} from '@angular/core';
import {Group} from './group.model';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {FormsModule, NgForm} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {AppService} from './app.service';
import {DocumentInputData} from './documentInputData.model';
import saveAs from 'file-saver';

/**
 * Root component of the WorkSheetGeneratorApp.
 *
 * This component manages the state and logic for generating worksheet documents.
 * It handles form input via Angular's template-driven forms, group selection logic,
 * form validation, error message display, and interaction with the backend service.
 */
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

  /**
   * Constructs the component and injects the required application service.
   *
   * @param {AppService} appService - The service responsible for backend interactions.
   */
  constructor(private appService: AppService) {}

  /**
   * Lifecycle hook that is called after component initialization.
   *
   * It triggers the initial loading of available groups by calling `getGroups()`.
   */
  ngOnInit() {
    this.getGroups();
  }

  /**
   * Retrieves all groups from the group service.
   * The retrieved groups are then assigned to the `this.groups` property.
   * In case of an error during the retrieval, an alert message displaying the error is shown.
   */
  public getGroups(): void {
    this.appService.getAll().subscribe({
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
   * Resets the selected group code afterward.
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
   * @param {number} index - The index of the group to remove.
   */
  public removeGroup(index: number): void {
    this.selectedGroups.splice(index, 1);
  }

  /**
   * Handles the form submission to generate a worksheet file in DOCX format.
   *
   * This method validates the form inputs.
   * If validation fails, it displays corresponding error messages for a limited time.
   * If the form is valid, it sends the data to the backend service and triggers a file download.
   *
   * @param {NgForm} form - The Angular form containing the document input data.
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

    let formData: DocumentInputData = new DocumentInputData(
      form.controls['professorName']?.value,
      form.controls['courseName']?.value,
      form.controls['assistantName']?.value,
      form.controls['place']?.value,
      this.selectedGroups
    );

    this.appService.getDocument(formData).subscribe( doc => {
      const courseName: string = formData.courseName.replace(/\s+/g, '_');
      const groupNames: string  = this.selectedGroups.map(g => g.code).join('_');
      saveAs(doc, `Fisa_de_protectia_muncii_${courseName}_${groupNames}.docx`);
    })
  }

  /**
   * Resets the form and clears related data such as selected groups and error messages.
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
