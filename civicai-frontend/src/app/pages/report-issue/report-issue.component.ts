import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ComplaintService } from '../../core/services/complaint.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-report-issue',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './report-issue.component.html',
  styleUrls: ['./report-issue.component.css']
})
export class ReportIssueComponent implements OnInit {
  complaintService = inject(ComplaintService);
  auth = inject(AuthService);
  router = inject(Router);

  categories: any[] = [];

  form = {
    categoryId: 1,
    description: '',
    address: '',
    contactNumber: '',
    latitude: 13.0827,
    longitude: 80.2707,
    photoBase64: ''
  };

  imagePreview: string | null = null;
  isSubmitting = false;

  // Duplicate Check Modal State
  possibleDuplicate: any = null;
  showDuplicateAlert = false;

  ngOnInit() {
    this.complaintService.getCategories().subscribe(cats => {
      this.categories = cats;
    });

    const user = this.auth.currentUser();
    if (user) {
      this.form.contactNumber = user.mobile || '';
    }
  }

  useCurrentLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(pos => {
        this.form.latitude = pos.coords.latitude;
        this.form.longitude = pos.coords.longitude;
        this.form.address = `GPS Coordinates (${pos.coords.latitude.toFixed(4)}, ${pos.coords.longitude.toFixed(4)}) - Near Main Road Zone`;
      }, err => {
        this.form.address = 'Main Road Street Area';
      });
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
        this.form.photoBase64 = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onDescriptionBlur() {
    if (this.form.description && this.form.description.length > 10) {
      this.complaintService.checkDuplicate(this.form.categoryId, this.form.description, this.form.address, this.form.latitude, this.form.longitude).subscribe(res => {
        if (res.is_duplicate && res.matched_complaints && res.matched_complaints.length > 0) {
          this.possibleDuplicate = res.matched_complaints[0];
          this.showDuplicateAlert = true;
        }
      });
    }
  }

  submitComplaint() {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.isSubmitting = true;
    this.complaintService.createComplaint(this.form).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        this.router.navigate(['/complaint', res.id]);
      },
      error: (err) => {
        this.isSubmitting = false;
        alert(err.error?.message || 'Error submitting complaint');
      }
    });
  }
}
