import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ComplaintService } from '../../core/services/complaint.service';
import { AdminService } from '../../core/services/admin.service';
import { AuthService } from '../../core/services/auth.service';
import { Complaint } from '../../core/models/all.models';

@Component({
  selector: 'app-department-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './department-dashboard.component.html',
  styleUrls: ['./department-dashboard.component.css']
})
export class DepartmentDashboardComponent implements OnInit {
  complaintService = inject(ComplaintService);
  adminService = inject(AdminService);
  auth = inject(AuthService);

  complaints: Complaint[] = [];
  filteredComplaints: Complaint[] = [];

  deptName = '';
  deptId: number | undefined;

  // Filter Bar
  statusFilter = '';
  priorityFilter = '';
  searchQuery = '';

  // Metrics
  metrics = {
    newIssues: 0,
    inProgress: 0,
    critical: 0,
    resolved: 0,
    duplicates: 0,
    falseSpam: 0
  };

  // Resolve Modal
  showResolveModal = false;
  selectedComplaint: Complaint | null = null;
  resolutionMessage = '';
  afterPhotoBase64 = '';
  afterPhotoPreview: string | null = null;

  // Flag Spam Modal
  showFlagModal = false;
  flagType = 'SPAM';
  flagReason = '';

  // User Risk & Block Modal
  showBlockModal = false;
  userRiskData: any = null;
  blockReason = '';

  ngOnInit() {
    const user = this.auth.currentUser();
    this.deptId = user?.departmentId;
    this.deptName = user?.departmentName || (user?.role === 'ROLE_SUPER_ADMIN' ? 'All Departments' : 'Department');
    this.loadComplaints();
  }

  loadComplaints() {
    this.complaintService.getDepartmentComplaints(this.deptId, this.statusFilter, this.priorityFilter).subscribe({
      next: (list) => {
        this.complaints = list;
        this.calculateMetrics();
        this.applyFilters();
      }
    });
  }

  calculateMetrics() {
    this.metrics.newIssues = this.complaints.filter(c => c.status === 'SUBMITTED' || c.status === 'AI_ANALYZED' || c.status === 'ASSIGNED').length;
    this.metrics.inProgress = this.complaints.filter(c => c.status === 'IN_PROGRESS' || c.status === 'ACKNOWLEDGED').length;
    this.metrics.critical = this.complaints.filter(c => c.priority === 'CRITICAL' && c.status !== 'RESOLVED' && c.status !== 'VERIFIED').length;
    this.metrics.resolved = this.complaints.filter(c => c.status === 'RESOLVED' || c.status === 'VERIFIED').length;
    this.metrics.duplicates = this.complaints.filter(c => c.status === 'DUPLICATE').length;
    this.metrics.falseSpam = this.complaints.filter(c => c.status === 'FALSE_SPAM').length;
  }

  applyFilters() {
    this.filteredComplaints = this.complaints.filter(c => {
      const matchSearch = !this.searchQuery ||
        c.complaintNumber.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        c.citizenName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        c.address.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        c.description.toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchSearch;
    });
  }

  updateStatus(c: Complaint, newStatus: string) {
    this.complaintService.updateStatus(c.id, newStatus, `Status updated by ${this.auth.currentUser()?.name}`).subscribe(updated => {
      c.status = updated.status;
      this.calculateMetrics();
    });
  }

  // Open Resolve Modal
  openResolve(c: Complaint) {
    this.selectedComplaint = c;
    this.resolutionMessage = '';
    this.afterPhotoBase64 = '';
    this.afterPhotoPreview = null;
    this.showResolveModal = true;
  }

  onAfterPhotoSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.afterPhotoPreview = e.target.result;
        this.afterPhotoBase64 = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  submitResolution() {
    if (!this.selectedComplaint || !this.afterPhotoBase64) {
      alert('After-resolution photograph proof is mandatory!');
      return;
    }

    this.complaintService.resolveComplaint(this.selectedComplaint.id, this.resolutionMessage, this.afterPhotoBase64).subscribe({
      next: (updated) => {
        this.selectedComplaint!.status = updated.status;
        this.selectedComplaint!.resolution = updated.resolution;
        this.showResolveModal = false;
        this.calculateMetrics();
        alert('Complaint successfully resolved with after-photo evidence!');
      }
    });
  }

  // Flag Modal
  openFlag(c: Complaint) {
    this.selectedComplaint = c;
    this.flagReason = '';
    this.showFlagModal = true;
  }

  submitFlag() {
    if (!this.selectedComplaint || !this.flagReason) return;
    this.adminService.flagComplaint(this.selectedComplaint.id, this.flagType, this.flagReason).subscribe(() => {
      this.selectedComplaint!.status = 'FALSE_SPAM';
      this.showFlagModal = false;
      this.calculateMetrics();
      alert('Complaint marked as False/Spam');
    });
  }

  // Block Modal
  openBlockUser(c: Complaint) {
    this.selectedComplaint = c;
    this.adminService.getUserRisk(c.citizenId).subscribe(risk => {
      this.userRiskData = risk;
      this.blockReason = '';
      this.showBlockModal = true;
    });
  }

  submitBlockUser() {
    if (!this.selectedComplaint || !this.blockReason) return;
    this.adminService.blockUser(this.selectedComplaint.citizenId, this.blockReason).subscribe(() => {
      this.showBlockModal = false;
      alert(`Citizen ${this.selectedComplaint?.citizenName} has been blocked.`);
    });
  }
}
