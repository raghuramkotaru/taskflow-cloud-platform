variable "region" {
  description = "AWS region to deploy into"
  type        = string
  default     = "us-east-1"
}

variable "project" {
  description = "Project name used as a resource prefix"
  type        = string
  default     = "taskflow"
}

variable "environment" {
  description = "Deployment environment name"
  type        = string
  default     = "dev"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "azs" {
  description = "Availability zones to spread subnets across"
  type        = list(string)
  default     = ["us-east-1a", "us-east-1b"]
}

variable "db_username" {
  description = "Master username for the RDS PostgreSQL instance"
  type        = string
  default     = "taskflow"
}

variable "db_password" {
  description = "Master password for RDS and DocumentDB (supply via TF_VAR or a secret manager in real use)"
  type        = string
  default     = "ChangeMe_InProd123"
  sensitive   = true
}

variable "service_names" {
  description = "Service names that each get an ECR repository"
  type        = list(string)
  default     = ["core-service", "ai-service", "frontend"]
}
