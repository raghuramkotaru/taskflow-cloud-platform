resource "aws_db_instance" "postgres" {
  identifier     = "${local.name_prefix}-postgres"
  engine         = "postgres"
  engine_version = "16"
  instance_class = "db.t3.micro"

  allocated_storage = 20
  storage_type      = "gp3"

  db_name  = "taskflow"
  username = var.db_username
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.data.id]

  multi_az            = false
  skip_final_snapshot = true
  publicly_accessible = false

  tags = local.tags
}

resource "aws_docdb_subnet_group" "main" {
  name       = "${local.name_prefix}-docdb-subnets"
  subnet_ids = aws_subnet.private[*].id
  tags       = local.tags
}

resource "aws_docdb_cluster" "mongo" {
  cluster_identifier = "${local.name_prefix}-docdb"
  engine             = "docdb"

  master_username = var.db_username
  master_password = var.db_password

  db_subnet_group_name   = aws_docdb_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.data.id]

  skip_final_snapshot = true

  tags = local.tags
}

resource "aws_docdb_cluster_instance" "mongo" {
  count              = 1
  identifier         = "${local.name_prefix}-docdb-${count.index}"
  cluster_identifier = aws_docdb_cluster.mongo.id
  instance_class     = "db.t3.medium"

  tags = local.tags
}
