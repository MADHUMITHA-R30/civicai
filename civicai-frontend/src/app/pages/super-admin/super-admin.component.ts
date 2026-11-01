import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ComplaintService } from '../../core/services/complaint.service';
import { AdminService } from '../../core/services/admin.service';
import { Department } from '../../core/models/all.models';

@Component({
  selector: 'app-super-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.css']
})
export class SuperAdminComponent implements OnInit {
  complaintService = inject(ComplaintService);
  adminService = inject(AdminService);

  departments: Department[] = [];

  // Citizens list simulation
  citizens = [
    { id: 7, name: 'Ramesh Kumar', mobile: '9876543210', total: 12, verified: 11, falseCount: 0, status: 'ACTIVE', risk: 'LOW' },
    { id: 8, name: 'Priya Sharma', mobile: '9876543211', total: 8, verified: 7, falseCount: 1, status: 'ACTIVE', risk: 'LOW' },
    { id: 9, name: 'Spam Bot User', mobile: '9111111111', total: 5, verified: 0, falseCount: 4, status: 'BLOCKED', risk: 'CRITICAL' }
  ];

  ngOnInit() {
    this.complaintService.getDepartments().subscribe(depts => {
      this.departments = depts;
    });
  }

  unblockUser(c: any) {
    this.adminService.unblockUser(c.id).subscribe(() => {
      c.status = 'ACTIVE';
      alert(`Citizen ${c.name} unblocked successfully.`);
    });
  }

  blockUser(c: any) {
    this.adminService.blockUser(c.id, 'Super Admin manual block').subscribe(() => {
      c.status = 'BLOCKED';
      alert(`Citizen ${c.name} blocked.`);
    });
  }
}
