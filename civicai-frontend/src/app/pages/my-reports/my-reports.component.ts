import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ComplaintService } from '../../core/services/complaint.service';
import { Complaint } from '../../core/models/all.models';

@Component({
  selector: 'app-my-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './my-reports.component.html',
  styleUrls: ['./my-reports.component.css']
})
export class MyReportsComponent implements OnInit {
  complaintService = inject(ComplaintService);

  complaints: Complaint[] = [];
  filteredComplaints: Complaint[] = [];

  statusFilter = 'ALL';
  searchQuery = '';

  ngOnInit() {
    this.complaintService.getMyComplaints().subscribe({
      next: (list) => {
        this.complaints = list;
        this.applyFilters();
      },
      error: () => {
        this.complaints = [];
        this.filteredComplaints = [];
      }
    });
  }

  applyFilters() {
    this.filteredComplaints = this.complaints.filter(c => {
      const matchStatus = this.statusFilter === 'ALL' || c.status === this.statusFilter;
      const matchQuery = !this.searchQuery ||
        c.complaintNumber.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        c.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        c.address.toLowerCase().includes(this.searchQuery.toLowerCase());

      return matchStatus && matchQuery;
    });
  }
}
