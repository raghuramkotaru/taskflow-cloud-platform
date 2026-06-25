terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.region

  # No real credentials are used. This configuration is validated with
  # `terraform validate` only; it is never applied against a live account.
  skip_credentials_validation = true
  skip_requesting_account_id  = true
  skip_metadata_api_check     = true

  access_key = "mock_access_key"
  secret_key = "mock_secret_key"
}
