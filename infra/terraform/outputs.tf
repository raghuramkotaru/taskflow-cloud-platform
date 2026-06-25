output "vpc_id" {
  description = "ID of the created VPC"
  value       = aws_vpc.main.id
}

output "eks_cluster_name" {
  description = "Name of the EKS cluster"
  value       = aws_eks_cluster.main.name
}

output "eks_cluster_endpoint" {
  description = "API server endpoint for the EKS cluster"
  value       = aws_eks_cluster.main.endpoint
}

output "rds_endpoint" {
  description = "Connection endpoint for the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.endpoint
}

output "docdb_endpoint" {
  description = "Connection endpoint for the DocumentDB cluster"
  value       = aws_docdb_cluster.mongo.endpoint
}

output "ecr_repository_urls" {
  description = "URLs of the ECR repositories per service"
  value       = { for name, repo in aws_ecr_repository.services : name => repo.repository_url }
}
